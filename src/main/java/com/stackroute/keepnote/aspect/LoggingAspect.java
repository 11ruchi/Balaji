package com.stackroute.keepnote.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* Annotate this class with @Aspect and @Component */

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	/*
	 * Write loggers for each of the methods of controller, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	
	/**
	 * Logger method for all the methods in the UserServiceImpl
	 * 
	 * @param joinPoint
	 */
	@Before("execution(* com.stackroute.keepnote.service.UserServiceImpl.*(..))")
	public void logBeforeAllUserMethods(JoinPoint joinPoint){
		LOGGER.info("Service call recieved in User Service");
	}
	
	/**
	 * Logger method for all the methods in service package
	 * 
	 * @param joinPoint
	 */
	@AfterReturning("execution(* com.stackroute.keepnote.service..*.*(..))")
	public void logAfterAllUserMethods(JoinPoint joinPoint){
		LOGGER.info("Service call completed");
	}
	
	@Pointcut("execution(* com.stackroute.keepnote.service.UserServiceImpl.registerUser(..))")
	public void registerUserPointCut(){
		
	}
	
	@Before("registerUserPointCut()")
	public void logBeforeRegisterUser(JoinPoint joinPoint){
		LOGGER.info("Initiating service call to register a new user");
	}
	
	@After("registerUserPointCut()")
	public void logAfterRegisterUser(JoinPoint joinPoint){
		LOGGER.info("Registration process completed for the user");
	}
	
	@AfterReturning("registerUserPointCut()")
	public void logAfterReturningRegisterUser(JoinPoint joinPoint){
		LOGGER.info("New user successfully registered in the system");
	}
	
	@AfterThrowing("registerUserPointCut()")
	public void logAfterThrowingRegisterUser(JoinPoint joinPoint){
		LOGGER.info("Unable to register the user as the user details are already availabe in the system");
	}
	
	@Around("execution(* com.stackroute.keepnote.service.UserServiceImpl.getUserById(..))")
	public Object logGetUserDetails(ProceedingJoinPoint joinPoint){
		
		LOGGER.info("Initiating service call to get details of a registered user");
		
		Object value = null;
		
		try{
			
			value = joinPoint.proceed();
			
		}catch(Throwable ex){
			
			LOGGER.info("User not registed in the system. Fetch operation failed");
			
		}
		
		LOGGER.info("Service call to get details of a registered user completed");
		
		return value;
	}
}
