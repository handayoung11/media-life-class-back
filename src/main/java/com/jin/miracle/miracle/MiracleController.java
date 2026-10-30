package com.jin.miracle.miracle;

import com.jin.miracle.miracle.dto.DashboardResDTO;
import com.jin.miracle.miracle.dto.MiracleFeedResDTO;
import com.jin.miracle.miracle.dto.SaveMiracleReqDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("miracle")
public class MiracleController {

    private final MiracleSvc miracleSvc;

    @ResponseStatus(code = CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long saveMiracle(@ModelAttribute @Valid SaveMiracleReqDTO s) {
        return miracleSvc.saveMiracle(s);
    }

    @GetMapping("/dashboard")
    public DashboardResDTO getDashboard() {
        return miracleSvc.getDashboard();
    }

    @GetMapping
    public MiracleFeedResDTO getMiracles(Pageable p) {
        return miracleSvc.getMiracles(p);
    }
}
