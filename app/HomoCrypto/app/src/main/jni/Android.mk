LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog

LOCAL_MODULE    := fullyhomo
LOCAL_SRC_FILES := fullyhomo.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/src/includes
APP_PLATFORM:=android-15

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := libgmpso
LOCAL_SRC_FILES := libgpm.so
include $(PREBUILT_SHARED_LIBRARY)


