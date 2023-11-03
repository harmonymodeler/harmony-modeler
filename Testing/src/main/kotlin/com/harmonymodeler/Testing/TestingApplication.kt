package com.harmonymodeler.Testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.command.CommandRegistration
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.CommandScan
import org.springframework.shell.command.annotation.EnableCommand
import org.springframework.stereotype.Component


@EnableCommand
@CommandScan
@SpringBootApplication
class TestingApplication {

//    @Bean
//    fun commandRegistration(): CommandRegistration {
//        return CommandRegistration.builder()
//                .build()
//    }
}


fun main(args: Array<String>) {
    runApplication<TestingApplication>(*args)
}

