package org.jalfrezi.server.resources;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jalfrezi.dao.SharePriceDao;
import org.jalfrezi.datamodel.SharePrice;
import org.jalfrezi.datamodel.id.ShareId;

@Named
@Path("/shareprices")
public class SharePricesResource {

	@Inject
	private SharePriceDao sharePriceDao;

	@GET
	@Path("{shareId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SharePrice> getCollection(@PathParam("shareId") ShareId shareId) throws SQLException {
		return sharePriceDao.findByShareId(shareId);
    }
}
