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
        String codetrim = code.replaceAll("\"", "");
        fileHandler.writeToFile(codeFile, codetrim);

        String command = "docker run -i --rm gcc bin/bash -c \"echo \'" + codetrim + "\' >> hello.cpp && cat hello.cpp \"";
        File commandFile = new File("src/main/java/ntnu/stud/tobakken/coderunner/docker/command.bat");
        fileHandler.writeToFile(commandFile, command);


        Process proc = Runtime.getRuntime().exec("docker run -i --rm gcc bin/bash -c \"echo \'" + codetrim + "\' >> hello.cpp && cat hello.cpp \" ");

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        StringBuilder output = new StringBuilder();
        StringBuilder outputError = new StringBuilder();

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = stdInput.readLine();
        System.out.println(s);
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            output.append(s).append("\n");
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
            outputError.append(s);
        }

        String outputString = output.toString();
        String outputErrorString = outputError.toString();

        System.out.println(outputString);
        if (outputErrorString.length() != 0) return outputErrorString;
        return outputString;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
