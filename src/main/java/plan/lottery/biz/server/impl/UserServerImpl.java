package plan.lottery.biz.server.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.data.dao.sql.util.sql.Logic;
import org.crap.data.dao.sql.util.sql.support.Condition;
import org.crap.data.dao.sql.util.sql.support.Profile;
import org.crap.data.dao.sql.util.sql.support.QueryBuilder;
import org.crap.data.dao.sql.util.sql.support.filter.SqlColumn;
import org.crap.data.dao.sql.util.support.ServiceDao;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryAdmin;
import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;

@Service
public class UserServerImpl implements UserServer {

	public static final Logger log = LogManager.getLogger(UserServerImpl.class);

	@Autowired
	private ServiceDao serviceDao;

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Override
	public LotteryUser getUser(String userName, String userPwd) {
		String sql = "SELECT * FROM lottery_user WHERE user_name=? and user_pwd=?";
		return serviceDao.get(sql, LotteryUser.class, new Object[] { userName, userPwd });
	}

	@Override
	public int updateUser(LotteryUser user) {
		int result = 0;
		try {
			serviceDao.getMapper().update(user);
			result = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("用户更新异常:", ex);
		}
		return result;
	}

	@Override
	public LotteryUser getUserByToken(String token) {
		String sql = "SELECT * FROM lottery_user WHERE token=?";
		return serviceDao.get(sql, LotteryUser.class, new Object[] { token });
	}

	@Override
	public int insertLoginLog(Integer uid, String ip) {
		String sql = "INSERT INTO lottery_user_login (uid,ip) VALUES (?,?)";
		return serviceDao.execute(sql, new Object[] { uid, ip });
	}

	@Override
	public LotteryAdmin getAdminUser(String userName, String userPwd) {
		String sql = "SELECT * FROM lottery_admin WHERE user_name=? and user_pwd=?";
		return serviceDao.get(sql, LotteryAdmin.class, new Object[] { userName, userPwd });
	}

	@Override
	public int updateAdminUser(Integer id, String token) {
		String sql = "UPDATE lottery_admin SET token=? WHERE id=?";
		return serviceDao.execute(sql, new Object[] { token, id });
	}

	@Override
	public DataResult getUserList(JSONObject params) {
		String sql = "SELECT * FROM lottery_user WHERE 1=1 ";
		QueryBuilder builder = new QueryBuilder(sql,
			new Condition(
				new SqlColumn(Logic.AND, "user_name").contanis(params.optString("user_name")),
				new SqlColumn(Logic.AND, "locked").equal(params.optString("locked"))),
			new Profile(params));
		DataResult result = serviceDao.queryForMapDataResult(builder);
		return result;
	}

	@Override
	public int addUser(LotteryUser user) {
		return (int) serviceDao.getMapper().save(user);
	}

	@Override
	public int lockUserAdmin(Integer id, Integer server_state) {
		String sql = "UPDATE lottery_user SET server_state=? WHERE id=?";
		return serviceDao.execute(sql, new Object[] { server_state, id });
	}

	@Override
	public int extension(Integer id, String server_end) {
		String sql = "UPDATE lottery_user SET server_end=? WHERE id=?";
		return serviceDao.execute(sql, new Object[] { server_end, id });
	}
}
