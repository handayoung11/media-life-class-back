package com.jin.miracle.miracle;

import com.jin.miracle.miracle.dto.MiracleFeedResDTO;
import org.springframework.data.domain.Pageable;

public interface MiracleDslRepo {
    MiracleFeedResDTO searchMiracles(Pageable pageable);
}
