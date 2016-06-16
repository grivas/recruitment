package com.viewtracker.repository;

import com.viewtracker.dto.View;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by gerriv on 28/08/2014.
 */
@RegisterMapper(value = {ViewTrackerRepository.ViewMapper.class})
public interface ViewTrackerRepository {
    @SqlUpdate("create table IF NOT EXISTS view_track (" +
            "user_id long, " +
            "viewer_id long, " +
            "viewed_on timestamp)")
    void createViewTable();

    @SqlUpdate("create index if not exists viewed_index on view_track(user_id)")
    void createViewedProfileIndex();

    /**
     * Inserts a new profile view in the view table
     * @param userId  id of the viewed profile
     * @param viewerId  id of the viewer
     * @param viewedOn  date and time in which the profile was viewed
     */
    @SqlUpdate("insert into view_track (user_id, viewer_id, viewed_on) values (:userId, :viewerId, :viewedOn)")
    void logView(@Bind("userId")long userId, @Bind("viewerId")long viewerId, @Bind("viewedOn")DateTime viewedOn);

    /**
     * Lists all views to the profile of the specified id
     * @param user_id
     * @param size  max size of the profile list
     * @param timeLimit  elements in the list must not be older than the specified date
     * @return  List containing profile views
     */
    @SqlQuery("select viewer_id, user_id, viewed_on from view_track where user_id = :userId and viewed_on > :time_limit limit :size")
    List<View> listViewersOf(@Bind("userId")long user_id, @Bind("size")int size, @Bind("time_limit")DateTime timeLimit);

    public static class ViewMapper implements ResultSetMapper<View> {
        @Override
        public View map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return View.newBuilder()
                    .withViewerId(resultSet.getLong("viewer_id"))
                    .withUserId(resultSet.getLong("user_id"))
                    .viewedOn(new DateTime(resultSet.getTimestamp("viewed_on"))).build();
        }
    }
}
