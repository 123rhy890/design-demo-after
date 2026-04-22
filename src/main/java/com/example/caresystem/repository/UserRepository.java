//package com.example.caresystem.repository;
//
//import com.example.caresystem.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    // 根据用户名查询用户
//    Optional<User> findByUsername(String username);
//
//    // 根据手机号查询用户
//    Optional<User> findByPhone(String phone);
//}
package com.example.caresystem.repository;

import com.example.caresystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户信息数据访问接口
 * @author rhy
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @return 用户实体
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户实体
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号和状态查找用户
     * @param phone 手机号
     * @param status 状态
     * @return 用户实体
     */
    Optional<User> findByPhoneAndStatus(String phone, Integer status);

    /**
     * 根据角色类型查找用户
     * @param roleType 角色类型
     * @return 用户列表
     */
    List<User> findByRoleType(Integer roleType);

    /**
     * 根据角色类型和状态查找用户
     * @param roleType 角色类型
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByRoleTypeAndStatus(Integer roleType, Integer status);

    /**
     * 统计指定角色的用户数量
     * @param roleType 角色类型
     * @return 用户数量
     */
    Long countByRoleType(Integer roleType);

    /**
     * 统计指定角色和状态的用户数量
     * @param roleType 角色类型
     * @param status 状态
     * @return 用户数量
     */
    Long countByRoleTypeAndStatus(Integer roleType, Integer status);

    /**
     * 根据状态查找用户
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByStatus(Integer status);

    /**
     * 根据创建时间范围查找用户
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 用户列表
     */
    List<User> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param status 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status, u.updateTime = :updateTime WHERE u.userId = :userId")
    int updateStatus(@Param("userId") Integer userId,
                     @Param("status") Integer status,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新用户角色
     * @param userId 用户ID
     * @param roleType 新角色
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.roleType = :roleType, u.updateTime = :updateTime WHERE u.userId = :userId")
    int updateRoleType(@Param("userId") Integer userId,
                       @Param("roleType") Integer roleType,
                       @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新密码
     * @param userId 用户ID
     * @param password 新密码
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password, u.updateTime = :updateTime WHERE u.userId = :userId")
    int updatePassword(@Param("userId") Integer userId,
                       @Param("password") String password,
                       @Param("updateTime") LocalDateTime updateTime);

    /**
     * 检查手机号是否已存在（排除指定用户）
     * @param phone 手机号
     * @param userId 要排除的用户ID
     * @return 是否存在
     */
    boolean existsByPhoneAndUserIdNot(String phone, Integer userId);

    /**
     * 检查邮箱是否已存在（排除指定用户）
     * @param email 邮箱
     * @param userId 要排除的用户ID
     * @return 是否存在
     */
    boolean existsByEmailAndUserIdNot(String email, Integer userId);

    /**
     * 根据关键字搜索用户
     * @param keyword 关键字（用户名、手机号、邮箱）
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE " +
            "u.username LIKE %:keyword% OR " +
            "u.phone LIKE %:keyword% OR " +
            "u.email LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
}
