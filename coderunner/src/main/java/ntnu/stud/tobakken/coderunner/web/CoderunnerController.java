package ntnu.stud.tobakken.coderunner.web;

import ntnu.stud.tobakken.coderunner.service.FileService;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@CrossOrigin
public class CoderunnerController {

    @PostMapping("/run")
    public String run(@RequestBody String code) throws IOException {
        FileService fileHandler = new FileService();
        File codeFile = new File("src/main/java/ntnu/stud/tobakken/coderunner/docker/cppCode.cpp");
        fileHandler.writeToFile(codeFile, code);

        String command = "docker run gcc:latest echo '" + code +
                "' >> cppCode.cpp" +
                " && gcc cppCode.cpp -lstdc++ && ./a.out";

        Process p = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        return "Hey";
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
