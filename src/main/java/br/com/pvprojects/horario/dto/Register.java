package br.com.pvprojects.horario.dto;

import java.io.Serializable;

public class Register implements Serializable {

    private static final long serialVersionUID = 2688490734896278400L;

    private Time_card time_card;
    private String _path = "/meu_ponto/registro_de_ponto";
    private Device _device;
    private String _appVersion = "0.10.32";

    public Register() {
        this.time_card = new Time_card();
        this._device = new Device();
    }

    public Time_card getTime_card() {
        return time_card;
    }

    public void setTime_card(Time_card time_card) {
        this.time_card = time_card;
    }

    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    public Device get_device() {
        return _device;
    }

    public void set_device(Device _device) {
        this._device = _device;
    }

    public String get_appVersion() {
        return _appVersion;
    }

    public void set_appVersion(String _appVersion) {
        this._appVersion = _appVersion;
    }

    private class Time_card {

        private Double latitude = new Double("-18.90860339");
        private Double longitude = new Double("-48.26135041");
        private String address = "Av. João Naves de Ávila, 1331 - Tibery, Uberlândia - MG, 38408-902, Brasil";
        private String reference_id = null;
        private boolean location_edited = false;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReference_id() {
            return reference_id;
        }

        public void setReference_id(String reference_id) {
            this.reference_id = reference_id;
        }

        public boolean isLocation_edited() {
            return location_edited;
        }

        public void setLocation_edited(boolean location_edited) {
            this.location_edited = location_edited;
        }
    }

    private class Device {

        private String manufacturer = null;
        private String model = null;
        private String uuid = null;
        private Browser browser;

        public Device() {
            this.browser = new Browser();
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Browser getBrowser() {
            return browser;
        }

        public void setBrowser(Browser browser) {
            this.browser = browser;
        }

        private class Browser {

            private String name = "Chrome";
            private String version = "77.0.3865.90";
            private String versionSearchString = "Chrome";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getVersionSearchString() {
                return versionSearchString;
            }

            public void setVersionSearchString(String versionSearchString) {
                this.versionSearchString = versionSearchString;
            }
        }
    }
}