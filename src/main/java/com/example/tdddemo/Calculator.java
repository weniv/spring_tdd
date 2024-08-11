package com.example.tdddemo;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Calculator {

    public int add(int a, int b) {
        return 0; // 임시로 0을 반환
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    // 곱하기 메소드는 아직 구현되지 않았습니다.
    // 테스트에서 @Disabled 어노테이션이 사용되었으므로, 여기서는 구현하지 않았습니다.
    public int multiply(int a, int b) {
        throw new UnsupportedOperationException("Multiplication is not implemented yet");
    }
}