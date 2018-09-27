package cn.intellif.distrubiton.lock.distrubitonlock;

import cn.intellif.distrubiton.lock.distrubitonlock.core.GlobalLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistrubitonLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistrubitonLockApplication.class, args);
		GlobalLock.lock(DistrubitonLockApplication.class);
		try{

		}finally {
			GlobalLock.unLock();
		}
	}
}
