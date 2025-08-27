package com.datn.ailms.interfaces;

import com.datn.ailms.model.entities.Bin;
import com.datn.ailms.model.entities.ProductDetail;

public interface IBinSelector {
    Bin binSelector(ProductDetail productDetail);
}
