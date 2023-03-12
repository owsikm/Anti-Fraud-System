package antifraud;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {

    @RequestMapping("/api/antifraud/transaction")
    @ResponseBody
    public PostResponse postResponse(@RequestBody PostRequest postRequest) {

        if (postRequest.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Long cash = postRequest.getAmount();
        PostResponse res = new PostResponse();

        if (cash <= 0 || cash.equals(null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        res.setResult(cash <= 200 ? "ALLOWED" : cash <= 1500 ? "MANUAL_PROCESSING" : "PROHIBITED");
        return res;
    }
}
