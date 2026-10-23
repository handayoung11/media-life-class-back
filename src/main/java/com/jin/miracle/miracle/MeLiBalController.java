package com.jin.miracle.miracle;

import com.jin.miracle.miracle.dto.SaveMelibalReqDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("miracle")
public class MeLiBalController {

    private final MeLiBalSvc meLiBalSvc;

    @ResponseStatus(code = CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveMelibal(@ModelAttribute @Valid SaveMelibalReqDTO s) {
        return meLiBalSvc.savemeLiBal(s);
    }
}
