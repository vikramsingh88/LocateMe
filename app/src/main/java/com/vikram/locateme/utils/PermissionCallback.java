package com.vikram.locateme.utils;

import java.util.HashMap;

public interface PermissionCallback {
    void onResponseReceived(HashMap<String, PermissionHelper.PermissionGrant> mapPermissionGrants);
}
