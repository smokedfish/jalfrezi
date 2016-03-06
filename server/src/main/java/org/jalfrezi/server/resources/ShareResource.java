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

import org.jalfrezi.dao.ShareDao;
import org.jalfrezi.datamodel.Share;
import org.jalfrezi.datamodel.id.IndexId;
import org.jalfrezi.datamodel.id.ShareId;

@Named
@Path("/shares")
public class ShareResource {

	@Inject
	private ShareDao shareDao;

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Share> get() throws SQLException {
		return shareDao.findByIndexId(IndexId.FTSE100);
    }

	@GET
	@Path("{shareId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Share get(@PathParam("shareId") ShareId id) throws SQLException {
		return shareDao.read(id);
    }
}
