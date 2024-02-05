package csi.attendence.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import csi.attendence.listener.events.OnRegisterUserEvent;
import csi.attendence.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnRegisterUserEventListener {

	private final EmailServiceImpl emailService;

	private final ApplicationEventPublisher eventPublisher;

	@Async
	@EventListener
	public void onRegisterUserComplete(OnRegisterUserEvent event) {

		this.emailService.sendVerificationEmailToUser(event.getUser(), event.getSiteURL());
	}

}
