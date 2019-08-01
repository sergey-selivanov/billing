package sssii.billing.common.entity.rs;

public class CustomerProjectCount {
    private Integer customerId;
    private Integer originalId;
    private Integer projectCount;


    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getProjectCount() {
        return projectCount;
    }
    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }
    public Integer getOriginalId() {
        return originalId;
    }
    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }
}
