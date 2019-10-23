package Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 凌浩,
 * @date 2019/8/21,
 * @time 11:45,
 */
@Slf4j
public final class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 常用的Excel文件格式
     */
    final class ExcelFileType {
        /**
         * EXCEL2003版本文件格式后缀
         */
        private final static String EXCEL2003 = "xls";

        /**
         * EXCEL2007版本文件格式后缀
         */
        private final static String EXCEL2007 = "xlsx";
    }

    /**
     * ExcelUtils中使用的无意义常量
     */
    final class Constant {

        /**
         * 第一个工作簿的号码
         */
        private final static int FIRST_SHEET = 0;

        private final static String SHEET_NAME_DEFAULT = "SHEET1";

        private final static String FILE_NAME_DEFAULT = "导出文件.xlsx";
    }

    /**
     * 读取Excel并解析为对象模型，
     * 主要通过WEB上传的MultipartFile格式机型解析
     * @param cls 对象模板的声明模板
     * @param file WEB传输的模板
     * @param <T> 对象模板的声明模板的泛型声明
     * @return T的对象集合
     */
    public static <T> List<T> readExcel(Class<T> cls, MultipartFile file) {

        List<T> datas = new Vector<>();

        if (null == file) {
            log.error("ExcelUtils解析上传Excel文件不存在错误！");
            return datas;
        }

        String fileName = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            InputStream is = file.getInputStream();

            if (fileName.endsWith(ExcelFileType.EXCEL2007)) {
                workbook = new XSSFWorkbook(is);
            } else if (fileName.endsWith(ExcelFileType.EXCEL2003)) {
                workbook = new HSSFWorkbook(is);
            } else {
                log.error("上传文件格式不正确！");
                return datas;
            }

            if (null != workbook) {
                /**
                 * 声明列名与T中注解ExcelColumn的属性组成映射关系，因可能存在对Field对应一列，故Key-Value使用String, List<Field>格式
                 */
                Map<String, List<Field>> classMap = new HashMap<>(32);
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(field -> {
                    ExcelColumn  annotation = field.getAnnotation(ExcelColumn.class);

                    if (null != annotation) {
                        String value = annotation.value();

                        if (StringUtils.isBlank(value)) {
                            return;
                        }

                        if (!classMap.containsKey(value)) {
                            classMap.put(value, new Vector<>());
                        }
                        field.setAccessible(true);
                        classMap.get(value).add(field);
                    }
                });

                /**
                 * 获取T的注解排序字段，并排序，对应Excel文件列的列号
                 */
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(rebuildNumber(classMap.size()));
                Sheet sheet = workbook.getSheetAt(Constant.FIRST_SHEET);

                boolean firstRow = true;

                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);

                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        if (null == row) {
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            boolean allBlank = true;

                            for (int k = row.getFirstCellNum(); k <= row.getLastCellNum(); k++) {
                                if (reflectionMap.containsKey(k)) {
                                    Cell cell = row.getCell(k);
                                    String cellValue = getCellValue(cell);

                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }

                                    List<Field> fieldList = reflectionMap.get(k);

                                    fieldList.forEach( field -> {
                                        try {
                                            handleField(t, cellValue, field);
                                        } catch (Exception e) {
                                            log.error("reflect field:{} -> value:{} exception", field.getName(), cellValue);
                                            log.error(e.toString(), e);
                                        }
                                    });
                                }
                            }

                            if ((!allBlank)) {
                                datas.add(t);
                            } else {
                                log.warn("第{}行整行为空！", i);
                            }
                        } catch (Exception e) {
                            log.error("第{}行转换时发生异常！", i);
                            log.error(e.toString(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Excel文件解析异常！");
            log.error(e.toString(), e);
        } finally {
            if (null != workbook) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error(e.toString(), e);
                }
            }
        }
        return datas;
    }

    /**
     * 获取单元格的属性值，转为String输出
     * @param cell 单元格
     * @return 单元格属性值的字符串形式
     */
    private static String getCellValue(Cell cell) {
        if (null == cell) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cellType == CellType.STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cellType == CellType.FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cellType == CellType.BLANK) {
            return "";
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }

    /**
     * 设置T的FieldList中指定的值
     * @param t 上下文涉及的T的实际对象
     * @param value 单元格属性
     * @param field 对象的成员属性对象类
     * @param <T> 上下文涉及的T类声明
     * @throws Exception 异常信息
     */
    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();

        if (null == type || type == void.class || StringUtils.isBlank(value)) {
            return;
        }

        if (type == Object.class) {
            field.set(t, value);
        } else if (null == type.getSuperclass() || Number.class == type.getSuperclass()) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    /**
     * 生成Excel的方法，适用于单Sheet页的形成，多Sheet暂不涉及
     * @param response 响应体
     * @param datas 数据集
     * @param targetFileName 拟形成文件的文件名
     * @param targetSheetName 第一个页签的名称，不指定时使用默认值
     * @param cls T的模板类
     * @param <T> T的模板
     */
    public static <T> void writeExcel(HttpServletResponse response,
                                      String targetFileName,
                                      String targetSheetName,
                                      List<T> datas, Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);

                    if (null != annotation && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return  false;
                })
                .sorted(Comparator.comparing(var -> var.getAnnotation(ExcelColumn.class).col()))
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(StringUtils.isBlank(targetSheetName) ? Constant.SHEET_NAME_DEFAULT : targetSheetName);
        AtomicInteger aRow = new AtomicInteger();

        /**
         * 输出Excel第一行的表头信息
         */
        {
            Row row = sheet.createRow(aRow.getAndIncrement());
            AtomicInteger aCol = new AtomicInteger();

            fieldList.forEach(var -> {
                Cell cell = row.createCell(aCol.getAndIncrement());
                cell.setCellValue(var.getAnnotation(ExcelColumn.class).value());
            });
        }

        if (CollectionUtils.isNotEmpty(datas)) {
            datas.forEach(var -> {
                Row row = sheet.createRow(aRow.getAndIncrement());
                AtomicInteger aCol = new AtomicInteger();
                fieldList.forEach(f -> {
                    Class<?> type = f.getType();
                    Object value = null;

                    try {
                        ExcelColumn annotation = f.getAnnotation(ExcelColumn.class);
                        value = f.get(var);
                        if (null != annotation) {
                            String innerName = annotation.innerSelectPropName();
                            if (StringUtils.isNotBlank(annotation.innerSelectPropName())) {
                                Object outer = f.get(var);
                                Field targetInnerField = outer.getClass().getDeclaredField(innerName);
                                targetInnerField.setAccessible(true);
                                value = targetInnerField.get(outer);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.toString(), e);
                    }
                    Cell cell = row.createCell(aCol.getAndIncrement());

                    if (null != value) {
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        /**
         * 冻结窗体
         * 即第一行和第一列
         */
        workbook.getSheetAt(Constant.FIRST_SHEET).createFreezePane(0, 1, 0, 1);

        String finalName = StringUtils.isBlank(targetFileName) ? Constant.FILE_NAME_DEFAULT : targetFileName;
        buildExcelDocument(finalName, workbook, response);
//        buildExcelFile("D:\\test.xlsx", workbook);
    }

    /**
     * 不生成文件而通过浏览器下载Excel文件
     * @param fileName 下载后的文件名称
     * @param workbook Excel的工作簿
     * @param response WEB请求的响应体对象
     */
    private static void buildExcelDocument(String fileName, Workbook workbook, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    /**
     * 生成Excelwe年
     * @param path 文件的全路径
     * @param workbook Excel的工作簿
     */
    private static  void buildExcelFile(String path, Workbook workbook) {
        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }

        try {
            workbook.write(new FileOutputStream(file));
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    /**
     * 随机整数num，获取大于num的最小的2的倍数
     * @param num 随机整数
     * @return rebuild
     */
    private static int rebuildNumber(int num) {
        int max = 1 << 30;
        num |= num >>> 1;
        num |= num >>> 2;
        num |= num >>> 4;
        num |= num >>> 8;
        num |= num >>> 16;
        return num < 0 ? 1 : (num >= max ? max : num + 1);
    }
}
