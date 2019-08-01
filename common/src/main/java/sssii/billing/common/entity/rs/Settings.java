package sssii.billing.common.entity.rs;

import java.util.Map;

import sssii.billing.common.entity.BillingEntity;
import sssii.billing.common.entity.Setting;

public class Settings implements BillingEntity
{
    private Map<String, Setting> settings;

    public Map<String, Setting> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Setting> settings) {
        this.settings = settings;
    }

    @Override
    public Integer getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(Integer id) {
        // TODO Auto-generated method stub

    }
}
