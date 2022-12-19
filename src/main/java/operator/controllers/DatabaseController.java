package operator.controllers;

import operator.configurations.DatabaseConfig;
import operator.services.ConsoleService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class DatabaseController {

    private final ResourceLoader resourceLoader;

    public DatabaseController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/database/index")
    public String databaseIndex(Model model) {
        model.addAttribute("files", getBackupsList());
        return "database/Index";
    }

    @GetMapping("/database/downloadScript")
    public ResponseEntity<Resource> databaseDownloadScript(@RequestParam(value = "withData", required = false) Boolean withData) {
        DatabaseConfig.init();
        String command = String.format("mysqldump --no-tablespaces --column-statistics=0 -u%s -p%s -h%s %s > %s",
                DatabaseConfig.username,
                DatabaseConfig.password,
                DatabaseConfig.host,
                DatabaseConfig.dbName,
                "scripts/" + DatabaseConfig.dbName + ".sql");
        if (withData == null)
            command += " --no-data";
        try {
            ConsoleService.execute(command);
            Resource resource = resourceLoader.getResource(Paths.get("scripts/" + DatabaseConfig.dbName + ".sql").toUri().toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/database/createBackup")
    public String databaseCreateBackup() {
        DatabaseConfig.init();
        try {
            ConsoleService.execute(String.format("mysqldump --column-statistics=0 -u%s -p%s -h%s --databases %s > %s",
                    DatabaseConfig.username,
                    DatabaseConfig.password,
                    DatabaseConfig.host,
                    DatabaseConfig.dbName,
                    "backups/" +  new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss_").format(new Date()) + DatabaseConfig.dbName + ".sql"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/database/index";
    }

    @PostMapping("/database/deleteBackup")
    public String databaseDeleteBackup(@RequestParam("fileName") String fileName) throws IOException {
        Files.deleteIfExists(new File(Paths.get("backups/" + fileName).toUri()).toPath());
        return "redirect:/database/index";
    }

    @PostMapping("/database/uploadBackup")
    public String databaseUploadBackup(@RequestParam("file") MultipartFile file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(Paths.get("backups/" + "customBackup.sql").toUri()));
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        restoreData("customBackup.sql", "scripts");
        return "redirect:/database/index";
    }

    @PostMapping("database/restoreBackup")
    public String databaseRestoreBackup(@RequestParam("fileName") String fileName) {
        restoreData(fileName, "backups");
        return "redirect:/database/index";
    }

    private void restoreData(String fileName, String folder) {
        DatabaseConfig.init();
        File file = new File(Paths.get(folder + "/" + fileName).toUri());
        String command = String.format("mysql -u%s -p%s -h%s %s < %s",
                DatabaseConfig.username,
                DatabaseConfig.password,
                DatabaseConfig.host,
                DatabaseConfig.dbName,
                file.toPath());
        try {
            ConsoleService.execute(command);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getBackupsList() {
        List<String> backups = new ArrayList<>();
        for (File backup : new File(Paths.get("backups").toUri()).listFiles())
            if (backup.isFile())
                backups.add(backup.getName());
        Collections.reverse(backups);
        return backups;
    }

}