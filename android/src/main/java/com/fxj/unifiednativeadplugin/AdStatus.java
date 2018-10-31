package com.fxj.unifiednativeadplugin;

public enum  AdStatus {
    CREATED,
    LOADING,
    FAILED,
    PENDING, // The ad will be shown when status is changed to LOADED.
    LOADED,
}
