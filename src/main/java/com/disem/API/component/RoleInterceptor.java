package com.disem.API.component;

import com.disem.API.enums.OrdersServices.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RoleInterceptor.class);

    private static final List<RoleEnum> TIPOS_PERMITIDOS = List.of(
            RoleEnum.ADMIN,
            RoleEnum.COLABORADOR_I,
            RoleEnum.COLABORADOR_II
    );

    private static final List<RoleEnum> SOMENTE_ADMIN = List.of(
            RoleEnum.ADMIN
    );

    private static final List<String> ROTAS_ADMIN = List.of(
            "/api/webservice/usuarios",
            "/api/webservice/remover-usuario",
            "/api/webservice/atualizar-usuario"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.contains("/webservice/login") || path.contains("/webservice/token") || path.contains("/webservice/buscar-usuario") || path.contains("/webservice/salvar-usuario")) {
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

            // 🔐 Verifica se é uma rota apenas para ADMIN
            if (ROTAS_ADMIN.contains(path)) {
                if (!isAdmin(userRole)) {
                    logger.warn("Acesso negado! Papel não autorizado para esta rota: {}", userRole);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Acesso negado");
                    return false;
                }
            } else {
                // 🔓 Para outras rotas, verifica o acesso normal
                if (!isAuthorized(userRole)) {
                    logger.warn("Acesso negado para o papel: {}", userRole);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Acesso negado");
                    return false;
                }
            }

            logger.info("Acesso autorizado para o papel: {}", userRole);
            return true;

        } catch (Exception e) {
            logger.error("Erro ao interpretar papel: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Papel inválido");
            return false;
        }
    }

    private boolean isAuthorized(RoleEnum userRole) {
        return TIPOS_PERMITIDOS.contains(userRole);
    }

    private boolean isAdmin(RoleEnum userRole) {
        return SOMENTE_ADMIN.contains(userRole);
    }
}
