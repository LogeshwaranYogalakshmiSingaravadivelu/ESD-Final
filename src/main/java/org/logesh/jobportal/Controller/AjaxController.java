package org.logesh.jobportal.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AjaxController {

    @GetMapping("/ajax")
    public Map<String, String> ajaxRequest() {
        Map<String, String> resp = new HashMap<String, String>();
        resp.put("key1", "value1");
        resp.put("key2", "value2");
        resp.put("key3", "value3");
        resp.put("key4", "value4");
        return resp;
    }
}
