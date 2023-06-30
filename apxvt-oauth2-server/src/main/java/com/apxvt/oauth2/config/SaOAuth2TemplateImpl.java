/**
 * @projectName Pyd-SynergisticAI
 * @package com.pydance.oauth2.config
 * @className com.pydance.oauth2.config.SaOAuth2TemplateImpl
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.oauth2.config;


import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import org.springframework.stereotype.Component;
/**
 * SaOAuth2TemplateImpl
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/6/30 11:28
 */
@Component
public class SaOAuth2TemplateImpl extends SaOAuth2Template {

    // 根据 id 获取 Client 信息
    @Override
    public SaClientModel getClientModel(String clientId) {
        // 此为模拟数据，真实环境需要从数据库查询
        if ("f4883dc4682cdba14d91".equals(clientId)) {
            return new SaClientModel()
                    .setClientId("f4883dc4682cdba14d91")
                    .setClientSecret("909b237528fb499086cd9466dcc575463363aa89")
                    .setAllowUrl("*")
                    .setContractScope("userinfo")
                    .setIsAutoMode(true);
        }
        return null;
    }

    // 根据ClientId 和 LoginId 获取openid
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // 此为模拟数据，真实环境需要从数据库查询
        return "gr_SwoIN0MC1ewxHX_vfCW3BothWDZMMtx__";
    }

}

 