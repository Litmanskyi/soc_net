package com.socnet.entity.enumaration;

public enum AssetType {
    IMAGE(){
        @Override
        public String toString() {
            return "Image";
        }
    }, VIDEO, AUDIO, FILE
}
