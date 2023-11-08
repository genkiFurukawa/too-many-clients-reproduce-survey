package hoshi.no.carby.reproduce.code.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleService {

    @Transactional
    public String hello() {
        return "hello";
    }
}
