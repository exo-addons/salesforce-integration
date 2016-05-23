@Application
@Portlet
@Bindings({
        @Binding(OrganizationService.class),
        @Binding(SalesforceLogin.class)
})

@Stylesheets({
        @Stylesheet(value = "/org/exoplatform/salesforce/portlets/profile/assets/profile.css", location = AssetLocation.APPLICATION, id = "profile")
})



@Less(value = {"profile.less"}, minify = true)
@Assets({"*"})
package org.exoplatform.salesforce.portlets.profile;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.asset.Stylesheets;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;
import org.exoplatform.salesforce.service.SalesforceLogin;
import org.exoplatform.services.organization.OrganizationService;
