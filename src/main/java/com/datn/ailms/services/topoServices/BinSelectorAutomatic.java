//package com.datn.ailms.services.topoServices;
//
//import com.datn.ailms.interfaces.IBinSelector;
//import com.datn.ailms.model.entities.product_entities.ProductDetail;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class BinSelectorAutomatic implements IBinSelector {
//
//    private final BinRepository _binRepository;
//
//    @Override
//    public Bin binSelector(ProductDetail productDetail) {
//        List<Bin> cands = _binRepository.findAvailableForProduct(productDetail.getProduct().getId());
//        if(cands.isEmpty()) return null;
//        return cands.get(0);
//    }
//}
