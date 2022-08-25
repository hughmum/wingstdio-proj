package com.mu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */

@Data
@TableName("borrow_record")
public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    private String deviceName;

    private LocalDateTime borrowTime;

    private String borrowReason;

    private LocalDateTime endTime;

    private LocalDateTime beginTime;

    private Integer borrowPersonId;

    private String refuseReason;

    private Integer teacherId;

    private String teacherReview;

    private Integer ownerId;

    private String ownerReview;

    private Integer administratorId;

    private String administratorReview;

    private Integer refusalId;

    private String failed;

    @TableField(exist = false)
    private List<Files> filesList;

    @TableField(exist = false)
    private String borrowPerson;

    @TableField(exist = false)
    private String ownerName;

    @TableField(exist = false)
    private String state;

    @TableField(exist = false)
    private Integer currentExamineId;

    @TableField(exist = false)
    private String refusePersonName;




}
