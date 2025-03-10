package com.disem.API.component;

import com.disem.API.enums.OrdersServices.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.contains("/webservice/login'") || path.contains("/webservice")) {
            return true;
        }
        String roleHeader = request.getHeader("X-Role");
        logger.info("Cabeçalho X-Role recebido: {}", roleHeader);

        if (roleHeader == null) {
            logger.warn("Role não fornecido");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Role not provided");
            return false;
        }
        try {
            int roleValue = Integer.parseInt(roleHeader.trim());
            RoleEnum userRole = RoleEnum.fromValue(roleValue);

            logger.info("Papel interpretado: {}", userRole);

            if (isAuthorized(userRole)) {
                logger.info("Acesso autorizado para o papel: {}", userRole);
                return true;
            } else {
                logger.warn("Acesso negado para o papel: {}", userRole);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access Denied");
                return false;
            }
        } catch (Exception e) {
            logger.error("Erro ao interpretar papel: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid Role");
            return false;
        }
    }
    private boolean isAuthorized(RoleEnum userRole) {
        return userRole == RoleEnum.ADMIN || userRole == RoleEnum.COLABORADOR_I || userRole == RoleEnum.COLABORADOR_II;
    }
}
