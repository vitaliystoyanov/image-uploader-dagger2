package com.developer.stoyanov.imageuploader.service;

public interface UploadServiceStatus {

    int STATUS_UNDEFINE = 0;
    int STATUS_PREPARE = 1;
    int STATUS_ERROR_FILEPATH = 6;
    int STATUS_COMPRESS = 3;
    int STATUS_NETWORK_ERROR = 7;
    int STATUS_RESPONSE = 5;
    int STATUS_SUCCESS_FINISHED = 8;
    int STATUS_UPLOAD = 4;
    int STATUS_LARGE_FILE = 2;

}
