package ntnu.stud.tobakken.coderunner.web;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CoderunnerController {

    @PostMapping("/run")
    public String run(@RequestBody String code){
        System.out.println("Postrequest");
        return "Hey";
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }
}
