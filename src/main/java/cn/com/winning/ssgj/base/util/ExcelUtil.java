package cn.com.winning.ssgj.base.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.ResolverStyle;
import java.util.*;

/**
 * @author chenshijie
 * @title
 * @email chensj@winning.com.cm
 * @package cn.com.winning.ssgj.base.util
 * @date 2018-03-13 13:52
 */
public class ExcelUtil {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static void main(String[] args) {
      /* List<Map> param = new ArrayList<Map>();
        for (int i=0;i<50;i++){
           Map m = new HashMap();
           m.put("BankName","test00"+i);
            m.put("Addr","test01"+i);
            m.put("Phone","test02"+i);
            param.add(m);
       }
       List<String> colList = new ArrayList<String>();
        colList.add("BankName");
        colList.add("Addr");
        colList.add("Phone");
       writeExcel(param,colList,3,"d:/a.xlsx");*/
        List<List<Object>> aaa = new ArrayList<>();
        try {
            aaa = importExcel("D:/download/userinfo.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (List<Object> objects : aaa) {
            StringBuilder sb = new StringBuilder();
            for (Object o : objects) {
                sb.append(o + ",");
            }
            System.out.println(sb);
        }
    }

    /**
     * 书写导入数据的模板
     * @param response
     * @param columnNameOfFirstRow 第一行数据的列明
     * @param workBook Excel
     * @param validateRoles 数据有效性
     * @param filename 文件名
     */
    public static void writeTemplateExcel(
                                  HttpServletResponse response,
                                  List<String> columnNameOfFirstRow,
                                  Workbook workBook,
                                  List<Map<String,Object>> validateRoles,
                                  String filename){
        OutputStream out = null;
        try {
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();
            //样式
            CellStyle cellStyle = workBook.createCellStyle();
            Font font=workBook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            //设置Excel数据有效性
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

            XSSFDataValidationConstraint dvConstraint = null;
            CellRangeAddressList addressList = null;
            XSSFDataValidation validation = null;

            for (Map<String, Object> validateRole : validateRoles) {
                dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint((String[]) validateRole.get("roles"));
                addressList = new CellRangeAddressList(Integer.parseInt(validateRole.get("firstRow").toString()),
                        Integer.parseInt(validateRole.get("lastRow").toString()),
                         Integer.parseInt(validateRole.get("firstCol").toString()), Integer.parseInt(validateRole.get("lastCol").toString()));
                validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
                //数据有效性对象
                sheet.addValidationData(validation);
            }

            //第一行保存列名
            Row colRow = sheet.createRow(0);
            for (int i = 0; i < columnNameOfFirstRow.size(); i++) {
                Cell cell=colRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnNameOfFirstRow.get(i).toString());
                sheet.setColumnWidth(i, 20 * 256);
            }
            //获取响应输出流
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 设置response的Header
            response.setContentType("application/msexcel;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 导出Excel
     * @param dataList      数据Map
     * @param colList       Map中列名称
     * @param cloumnCount   列数
     * @param finalXlsxPath Excel放置位置
     * @param sheetIndex   工作表下标
     */
    public static void writeExcel(List<Map> dataList, List<String> colList, int cloumnCount, String finalXlsxPath,int sheetIndex) {
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = cloumnCount;
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbook(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(sheetIndex);
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();  // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    sheet.removeRow(row);
                }

            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                Map dataMap = dataList.get(j);
                for (int k = 0; k < columnNumCount; k++) {
                    // 在一行内循环
                    Cell cell = row.createCell(k);
                    String value = "";
                    if (dataMap.get(colList.get(k)) != null) {
                        value = dataMap.get(colList.get(k)).toString();
                    }

                    cell.setCellValue(value);

                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }
    /**
     * 导出Excel
     * @param dataList      数据Map
     * @param colList       Map中列名称
     * @param cloumnCount   列数
     * @param finalXlsxPath Excel放置位置
     */
    public static void writeExcel(List<Map> dataList, List<String> colList, int cloumnCount, String finalXlsxPath) {
        writeExcel(dataList,colList,cloumnCount,finalXlsxPath,0);
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    public static List<List<Object>> importExcel(String fileName, int startSheet) throws Exception {
        List<List<Object>> list = null;

        //创建Excel工作薄
        File finalXlsxFile = new File(fileName);
        Workbook work = getWorkbook(finalXlsxFile);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = startSheet; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            list.addAll(resolveSheetData(sheet));
        }
        return list;
    }

    public static List<List<Object>> importExcel(String fileName) throws Exception {
        List<List<Object>> list = null;

        //创建Excel工作薄
        File finalXlsxFile = new File(fileName);
        Workbook work = getWorkbook(finalXlsxFile);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        list = new ArrayList<List<Object>>();
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            list.addAll(resolveSheetData(sheet));
        }
        return list;
    }

    private static List<List<Object>> resolveSheetData(Sheet sheet) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Row row = null;
        Cell cell = null;
        //遍历当前sheet中的所有行
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null || row.getFirstCellNum() == j) {
                continue;
            }

            //遍历所有的列
            List<Object> li = new ArrayList<Object>();
            for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                cell = row.getCell(y);
                li.add(getCellValue(cell));
            }
            list.add(li);
        }
        return list;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //日期格式化
        DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                short format = cell.getCellStyle().getDataFormat();
                if (format == 14 || format == 31 || format == 57 || format == 58
                        || (176<=format && format<=178) || (182<=format && format<=196)
                        || (210<=format && format<=213) || (208==format ) ) { // 日期
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    value =  sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
                }else  if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                }else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 导出Excel
     *
     * @param dataList 数据Map
     * @param colList  Map中列名称：列名
     * @param response HTTP 响应
     * @param workBook Excel工作簿
     * @param filename Excel导出文件名
     */
    public static void exportExcelByStream(List<Map> dataList, List<String> colList, List<String> tableNameList, HttpServletResponse response, Workbook workBook, String filename) {
        try {
            //样式
            CellStyle cellStyle = workBook.createCellStyle();
            Font font=workBook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

            //除去第一行边框
            CellStyle cellStyle1 = workBook.createCellStyle();
            cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            cellStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();
            //第一行保存列名
            Row colRow = sheet.createRow(0);
            if (tableNameList == null) {
                for (int i = 0; i < colList.size(); i++) {
                    Cell cell=colRow.createCell(i);
                    cell.setCellValue(colList.get(i).toString());
                    cell.setCellStyle(cellStyle);
                    sheet.setColumnWidth(i, 20 * 256);
                }
            } else {
                for (int i = 0; i < tableNameList.size(); i++) {
                    Cell cell=colRow.createCell(i);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(tableNameList.get(i).toString());
                    sheet.setColumnWidth(i, 20 * 256);
                }
            }

            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                Map dataMap = dataList.get(j);
                for (int k = 0; k < colList.size(); k++) {
                    // 在一行内循环
                    Cell cell = row.createCell(k);
                    String value = "";
                    if (dataMap.get(colList.get(k)) != null) {
                        value = dataMap.get(colList.get(k)).toString();
                    }
                    cell.setCellValue(value);
                    cell.setCellStyle(cellStyle1);
                }
            }
            //获取响应输出流
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 设置response的Header
            response.setContentType("application/msexcel;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("数据导出成功");
    }


    /**
     * 书写导入数据的模板
     * @param response
     * @param columnNameOfFirstRow 第一行数据的列明
     * @param workBook Excel
     * @param validateRoles 数据有效性
     * @param filename 文件名
     */
    public static void writeTemplateExcel(
            HttpServletResponse response,
            List<String> columnNameOfFirstRow,
            Workbook workBook,
            List<Map<String,Object>> validateRoles,
            List<Map<String,Object>> hiddenValidateRoles,
            String filename){
        OutputStream out = null;
        try {
            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();
            Sheet hidden = null;
            DataValidationConstraint dvConstraint = null;
            CellRangeAddressList addressList = null;
            DataValidation validation = null;
            //多值匹配使用hidden sheet
            for (int j = 0; j < hiddenValidateRoles.size(); j++) {
                hidden = workBook.createSheet("hidden"+j);
                Cell cell1 = null;
                Map<String, Object> hiddenValidateRole =  hiddenValidateRoles.get(j);
                String[] roles = (String[]) hiddenValidateRole.get("roles");
                for (int i = 0, length= roles.length; i < length; i++) {
                    String name = roles[i];
                    Row row = hidden.createRow(i);
                    cell1 = row.createCell(0);
                    cell1.setCellValue(name);
                }

                addressList =
                        new CellRangeAddressList(Integer.parseInt(hiddenValidateRole.get("firstRow").toString()),
                                Integer.parseInt(hiddenValidateRole.get("lastRow").toString()),
                                Integer.parseInt(hiddenValidateRole.get("firstCol").toString()),
                                Integer.parseInt(hiddenValidateRole.get("lastCol").toString()));
                Name namedCell = workBook.createName();
                namedCell.setNameName("hidden"+j);
                namedCell.setRefersToFormula("hidden"+j+"!$A$1:$A$" + roles.length);
                dvConstraint = DVConstraint.createFormulaListConstraint("hidden"+j);
                validation = new HSSFDataValidation(addressList, dvConstraint);

                workBook.setSheetHidden(j+1, true);
                sheet.addValidationData(validation);
            }

            //样式
            CellStyle cellStyle = workBook.createCellStyle();
            Font font=workBook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            //设置Excel数据有效性
            //DataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);



            for (Map<String, Object> validateRole : validateRoles) {
                dvConstraint =   DVConstraint.createExplicitListConstraint((String[]) validateRole.get("roles"));
                addressList = new CellRangeAddressList(
                        Integer.parseInt(validateRole.get("firstRow").toString()),
                        Integer.parseInt(validateRole.get("lastRow").toString()),
                        Integer.parseInt(validateRole.get("firstCol").toString()),
                        Integer.parseInt(validateRole.get("lastCol").toString()));
                validation = new HSSFDataValidation(addressList, dvConstraint);
                //数据有效性对象
                sheet.addValidationData(validation);
            }

            //第一行保存列名
            Row colRow = sheet.createRow(0);
            for (int i = 0; i < columnNameOfFirstRow.size(); i++) {
                Cell cell = colRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnNameOfFirstRow.get(i).toString());
                sheet.setColumnWidth(i, 20 * 256);
            }
            //获取响应输出流
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 设置response的Header
            response.setContentType("application/msexcel;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
