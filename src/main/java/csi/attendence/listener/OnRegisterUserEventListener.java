package csi.attendence.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import csi.attendence.listener.events.OnRegisterUserEvent;
import csi.attendence.service.impl.EmailServiceImpl;

@Component

public class OnRegisterUserEventListener {

	private final EmailServiceImpl emailService;

	private final ApplicationEventPublisher eventPublisher;

	@Async
	@EventListener
	public void onRegisterUserComplete(OnRegisterUserEvent event) {

		this.emailService.sendVerificationEmailToUser(event.getUser(), event.getSiteURL());

	}

	public OnRegisterUserEventListener(EmailServiceImpl emailService, ApplicationEventPublisher eventPublisher) {
		super();
		this.emailService = emailService;
		this.eventPublisher = eventPublisher;
	}
}
