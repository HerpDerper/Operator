package operator.services;

import operator.models.Cheque;
import operator.models.Employee;
import operator.models.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ExcelService {

    private final XSSFWorkbook xssfWorkbook;

    private XSSFSheet sheet;

    private final List<Cheque> chequeList;

    public ExcelService(List<Cheque> chequeList) {
        this.chequeList = chequeList;
        xssfWorkbook = new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer)
            cell.setCellValue((Integer) value);
        else if (value instanceof Boolean)
            cell.setCellValue((Boolean) value);
        else
            cell.setCellValue(value.toString());
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Cheque cheque : chequeList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            Product product = cheque.getProduct();
            Employee employee = cheque.getEmployee();
            String employeeFullName = employee.getPassportData().getSurname() +
                    " " + employee.getPassportData().getName() +
                    " " + employee.getPassportData().getPatronymic();
            String fullPrice = String.valueOf(product.getPrice() * cheque.getProductCount());
            String employeeData = employee.getEmail() + " - " + employee.getPhone();
            createCell(row, columnCount++, cheque.getDateCreation(), style);
            createCell(row, columnCount++, cheque.getProduct().getName(), style);
            createCell(row, columnCount++, cheque.getProduct().getPrice(), style);
            createCell(row, columnCount++, cheque.getProductCount(), style);
            createCell(row, columnCount++, fullPrice, style);
            createCell(row, columnCount++, employeeFullName, style);
            createCell(row, columnCount++, employeeData, style);
        }
    }

    private void writeHeaderLine() {
        sheet = xssfWorkbook.createSheet("Cheque");
        Row row = sheet.createRow(0);
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Дата продажи", style);
        createCell(row, 1, "Товар", style);
        createCell(row, 2, "Цена за единцу товара", style);
        createCell(row, 3, "Количество товара", style);
        createCell(row, 4, "Итоговая цена", style);
        createCell(row, 5, "ФИО сотрудника", style);
        createCell(row, 6, "Контактные данные сотрудника", style);
    }

    public void export(String currentDateTime) throws IOException {
        writeHeaderLine();
        writeDataLines();
        OutputStream outputStream = Files.newOutputStream(Paths.get("files/cheque_" + currentDateTime + ".xlsx"));
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
        outputStream.close();
    }

}