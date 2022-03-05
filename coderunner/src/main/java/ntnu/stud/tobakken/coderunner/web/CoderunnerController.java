package ntnu.stud.tobakken.coderunner.web;

import ntnu.stud.tobakken.coderunner.service.FileService;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;

@RestController
@CrossOrigin
public class CoderunnerController {

    @PostMapping("/run")
    public String run(@RequestBody Map<String, String> code) throws IOException {
        FileService fileHandler = new FileService();
        File f = new File("src/main/java/ntnu/stud/tobakken/coderunner/docker/cppCode");
        try {
            f.mkdir();
        } catch (Exception e){
            e.printStackTrace();
        }
        File codeFile = new File("src/main/java/ntnu/stud/tobakken/coderunner/docker/cppCode/temp.cpp");
        fileHandler.writeToFile(codeFile, code.get("code"));


/*        String command = "docker run -i --rm gcc bin/bash -c \"echo \'" + code.get("code") + "\' >> hello.cpp && cat hello.cpp \"";
        File commandFile = new File("src/main/java/ntnu/stud/tobakken/coderunner/docker/cppCode/command.bat");
        fileHandler.writeToFile(commandFile, command);*/


        Runtime.getRuntime().exec("docker rmi coderunner");
        Process p = Runtime.getRuntime().exec("docker build C:\\Users\\toroy\\Documents\\NTNU\\Nettverksprog\\NettverksprogKode\\IDATT2104Nettverksprog\\coderunner\\src\\main\\java\\ntnu\\stud\\tobakken\\coderunner\\docker\\ -t coderunner");
        BufferedReader buildInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader buildError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String b;
        while ((b = buildInput.readLine()) != null){
            System.out.println(b);
        }

        while ((b = buildError.readLine()) != null) {
            System.out.println(b);
        }

        FileSystemUtils.deleteRecursively(f);

        Process proc = Runtime.getRuntime().exec("docker run --rm coderunner");

        //This might work on linux-system
        //Process proc = Runtime.getRuntime().exec("src/main/java/ntnu/stud/tobakken/coderunner/docker/cppCode/command.bat");

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        StringBuilder output = new StringBuilder();
        StringBuilder outputError = new StringBuilder();

// Read the output from the command
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            output.append(s).append("\n");
        }

// Read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
            outputError.append(s);
        }


        String outputString = output.toString();
        String outputErrorString = outputError.toString();

        if (outputErrorString.length() != 0) return outputErrorString;
        return outputString;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
