package com.example.pullpush.custom;

import com.example.pullpush.enums.SearchModel;
import com.example.pullpush.enums.SearchType;
import com.example.pullpush.enums.StorageMode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RichParameters {

    private final StorageMode storageMode;

    private final SearchModel searchModel;

    private final String fromType;
}
