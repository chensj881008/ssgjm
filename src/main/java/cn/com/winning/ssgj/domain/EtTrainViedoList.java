package cn.com.winning.ssgj.domain;

import java.io.Serializable;
import org.apache.ibatis.type.Alias;
import java.util.Date;
import cn.com.winning.ssgj.domain.BaseDomain;

/**
 * Coder AutoGenerator generate.
 *
 * @author Coder AutoGenerator
 * @date 2018-02-24 10:57:35
 */
 
@Alias("etTrainViedoList")
public class EtTrainViedoList extends BaseDomain implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * @val ID
	 */
	private Long id;
	
	/**
	 * @val 合同ID
	 */
	private Long cId;
	
	/**
	 * @val 项目ID
	 */
	private Long pmId;
	
	/**
	 * @val 分类(产品ID)
	 */
	private Long pdId;
	
	/**
	 * @val 视频名称
	 */
	private String videoName;
	
	/**
	 * @val 视频时长
	 */
	private Date videoTime;
	
	/**
	 * @val 培训学习记录
	 */
	private Integer num;
	
	/**
	 * @val 视频播放路径
	 */
	private String videoPath;
	
	/**
	 * @val 备注
	 */
	private String remark;
	
	public EtTrainViedoList() {

	}

	/**
	 * @val ID
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @val ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @val 合同ID
	 */
	public Long getCId() {
		return cId;
	}
	
	/**
	 * @val 合同ID
	 */
	public void setCId(Long cId) {
		this.cId = cId;
	}
	
	/**
	 * @val 项目ID
	 */
	public Long getPmId() {
		return pmId;
	}
	
	/**
	 * @val 项目ID
	 */
	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}
	
	/**
	 * @val 分类(产品ID)
	 */
	public Long getPdId() {
		return pdId;
	}
	
	/**
	 * @val 分类(产品ID)
	 */
	public void setPdId(Long pdId) {
		this.pdId = pdId;
	}
	
	/**
	 * @val 视频名称
	 */
	public String getVideoName() {
		return videoName;
	}
	
	/**
	 * @val 视频名称
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	
	/**
	 * @val 视频时长
	 */
	public Date getVideoTime() {
		return videoTime;
	}
	
	/**
	 * @val 视频时长
	 */
	public void setVideoTime(Date videoTime) {
		this.videoTime = videoTime;
	}
	
	/**
	 * @val 培训学习记录
	 */
	public Integer getNum() {
		return num;
	}
	
	/**
	 * @val 培训学习记录
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	
	/**
	 * @val 视频播放路径
	 */
	public String getVideoPath() {
		return videoPath;
	}
	
	/**
	 * @val 视频播放路径
	 */
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	
	/**
	 * @val 备注
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * @val 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}