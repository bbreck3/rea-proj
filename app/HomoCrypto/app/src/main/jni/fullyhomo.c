/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include "types.h"







/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */



//JNIEXPORT int JNICALL convert(jint num,jchar result[]); // decimal to binary/
//int convert_btod(jint num); //binary to decimal
//void FHE(jint input);

/**global variables   */

jstring Java_com_example_homocrypto_HomomorphicCrypto_getNumbers( JNIEnv* env,
                                                  jobject this , jint value1, jint value2 )
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
    #else
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a (hard-float)"
      #else
        #define ABI "armeabi-v7a"
      #endif
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__x86_64__)
   #define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
   #define ABI "mips64"
#elif defined(__mips__)
   #define ABI "mips"
#elif defined(__aarch64__)
   #define ABI "arm64-v8a"
#else
   #define ABI "unknown"
#endif
    //format the output
    char *szFormat = "Num1... %i:  :Num2...%i";
    char *szResult;
    //sprintf
    // add the two values

        //jlong sum = value1+value2;

        // malloc room for the resulting string
        szResult = malloc(sizeof(szFormat) + 50);

        /**
            Encryption

        */


         int operatorNum = 1;
                int operandNum = operatorNum+1;

                char * operands[operandNum];
                char operators[operatorNum];

                fhe_pk_t pk;
                fhe_sk_t sk;
                fhe_pk_init(pk);
                fhe_sk_init(sk);

                //fhe_keygen(pk, sk);

                //mpz_t carry;
                //mpz_init(carry);
                //fhe_encrypt(carry, pk, 0);

                encryptedOperand encryptedOperands[3];

                /*  Oeration initialization                    */
                int i, j, k, operatorIndex, operandIndex, operandLength;
                for(i=0, operatorIndex=3; i<operatorNum; i++, operatorIndex+=2){
                    operators[i] = "+";//operation
                    printf("Operator #%d = %c\n", (i+1), operators[i]);
                }
                /**  Operand Initialization  */
                   for(j=0, operandIndex=2; j<operandNum; j++, operandIndex+=2){
                    //manually sets the operand lengthto the lenght of the converted char array--> this needs improovements to handle varibale length
                    operandLength = strlen(value1);//argv[operandIndex]);;
                    operands[j] = (char *)calloc(operandLength, sizeof(char));
                    encryptedOperands[j].operand = (mpz_t *)calloc(operandLength, sizeof(mpz_t));
                    encryptedOperands[j].length = operandLength;
                    //This sets the operands to the specific converted numbers
                     if(j==0) //first number
                        strcpy(operands[0], value1);//argv[operandIndex]);
                    if(j==1) //second number
                        strcpy(operands[1],value2);
                     mpz_init(encryptedOperands[j].operand[j]);
                    fhe_encryptBits(encryptedOperands[j].operand, operandLength, operands[j], pk);
                    printf("Operand #%d = %s\n", (j+1), operands[j]);
                    }


        /**
                Resutls Section:
                Print the encrpypted operation

        */
        // standard sprintf
       sprintf(szResult, szFormat, value1,value2);


        // get an object string
        jstring result = (*env)->NewStringUTF(env, szResult);

        // cleanup
        free(szResult);

        return result;


/**
        Test for the operation
*/

    /*switch(op){

       case "+":
                // add the two values
                 jlong sum = value1+value2;
                  // malloc room for the resulting string
                      szResult = malloc(sizeof(szFormat) + 20);

                      // standard sprintf
                      sprintf(szResult, szFormat, sum);

                      // get an object string
                      jstring result = (*env)->NewStringUTF(env, szResult);

                      // cleanup
                      free(szResult);

                      return result;
                  break;
       case "-":
                    // subtract the two values
                    jlong difference = value1-value2;
                    // malloc room for the resulting string
                        szResult = malloc(sizeof(szFormat) + 20);

                        // standard sprintf
                        sprintf(szResult, szFormat, difference);

                        // get an object string
                        jstring result = (*env)->NewStringUTF(env, szResult);

                        // cleanup
                        free(szResult);

                        return result;
                    break;
       case "/":
                    // divide the two values
                    jlong dividend = value1/value2;
                    // malloc room for the resulting string
                        szResult = malloc(sizeof(szFormat) + 20);

                        // standard sprintf
                        sprintf(szResult, szFormat, dividend);

                        // get an object string
                        jstring result = (*env)->NewStringUTF(env, szResult);

                        // cleanup
                        free(szResult);

                        return result;
                    break;
       case "*":
                   // multiply the two numbers the two values
                    jlong product = value1*value2;
                    // malloc room for the resulting string
                        szResult = malloc(sizeof(szFormat) + 20);

                        // standard sprintf
                        sprintf(szResult, szFormat, product);

                        // get an object string
                        jstring result = (*env)->NewStringUTF(env, szResult);

                        // cleanup
                        free(szResult);

                        return result;
                    break;
     // malloc room for the resulting string
    //szResult = malloc(sizeof(szFormat) + 20);

    // standard sprintf
    //sprintf(szResult, szFormat, sum);

    // get an object string
    //jstring result = (*env)->NewStringUTF(env, szResult);

    // cleanup
   // free(szResult);
}*/
    //return result;

}

