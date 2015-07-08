LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := fullyhomo
LOCAL_SRC_FILES := \
	/home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/main/jni/Application.mk \
	/home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/main/jni/libgmp.so \
	/home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/main/jni/Android.mk \
	/home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/main/jni/fullyhomo.c \

LOCAL_C_INCLUDES += /home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/main/jni
LOCAL_C_INCLUDES += /home/rob/Documents/workspace_Android/NDKTest/HomoCrypto/app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
