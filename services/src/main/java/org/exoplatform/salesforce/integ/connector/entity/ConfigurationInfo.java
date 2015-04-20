/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.exoplatform.salesforce.integ.connector.entity;

import org.chromattic.api.annotations.*;
import org.chromattic.ext.format.BaseEncodingObjectFormatter;


@PrimaryType(name = "sf:configurationInfo")
@FormattedBy(BaseEncodingObjectFormatter.class)
@NamingPrefix("sf")
public abstract class ConfigurationInfo {

  @Id
  public abstract String getId();

  @Name
  public abstract String getName();

  @Path
  public abstract String getPath();


	@Property(name = "sf:clientId")
	public abstract String getClientId();

	public abstract void setClientId(String clientId);

	@Property(name = "sf:clientSecret")
	public abstract String getClientSecret();

	public abstract void setClientSecret(String clientSecret);

	@Property(name = "sf:redirectUri")
	public abstract String getRedirectUri();

	public abstract void setRedirectUri(String redirectUri);




}