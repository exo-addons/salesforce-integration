/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.salesforce.integ.component.activity;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.social.webui.activity.BaseUIActivityBuilder;

import java.util.Map;

public class UISalesforceActivityBuilder extends BaseUIActivityBuilder {
  private static final Log LOG = ExoLogger.getLogger(UISalesforceActivityBuilder.class);

  public static final String DESCRIPTION_PARAM = "description";
  public static final String STAGE_PARAM = "stage";
  public static final String CLOSEDATE_PARAM = "closeDate";
  public static final String AMOUNT_PARAM = "amount";

  @Override
  protected void extendUIActivity(BaseUIActivity uiActivity, ExoSocialActivity activity) {
    UISalesforceActivity uiSalesforceActivity = (UISalesforceActivity) uiActivity;
    Map<String, String> templateParams = activity.getTemplateParams();
    uiSalesforceActivity.setDescription(templateParams.get(DESCRIPTION_PARAM));
    uiSalesforceActivity.setStage(templateParams.get(STAGE_PARAM));
    uiSalesforceActivity.setCloseDate(templateParams.get(CLOSEDATE_PARAM));
    uiSalesforceActivity.setAmount(templateParams.get(AMOUNT_PARAM));
  }
}
