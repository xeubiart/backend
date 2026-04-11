package com.xeubiart.identity.service;

import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.providers.IdentityProvider;
import com.xeubiart.identity.side_effects.SetCookieSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.session.SessionRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdentityServiceTest {

}
