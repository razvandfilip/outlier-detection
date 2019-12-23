package razvan.filip.outlierdetectionapp.outliers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutlierController {

    private static Logger logger = LoggerFactory.getLogger(OutlierController.class);

    private OutlierService outlierService;

    @Autowired
    public OutlierController(OutlierService outlierService) {
        this.outlierService = outlierService;
    }

    @GetMapping("/outliers")
    public String getOutliers(@RequestParam String publisherId, @RequestParam(defaultValue = "50") int windowSize) {

        logger.info("Got request for publisherId {} and windowSize {}", publisherId, windowSize);

        String result = outlierService.getOutliers(publisherId, windowSize);
        logger.info(result);
        return result;
    }
}
