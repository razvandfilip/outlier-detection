package razvan.filip.outlierdetectionapp.outliers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutlierController {

    private static Logger logger = LoggerFactory.getLogger(OutlierController.class);

    @RequestMapping("/hello")
    public String hello() {
        logger.info("hello");
        return "hello";
    }
}
