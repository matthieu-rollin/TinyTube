package com.epita.mti.tinytube.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by _Gary_ on 28/11/2014.
 * Mapping of the Tinytube JSON for Jackson
 */
public class TinytubeModel extends Model {
    /**
     * The TAG for logs
     */
    private static final String TAG = TinytubeModel.class.getSimpleName();

    /**
     * Mapping of the JSON object Item
     */
    public static class Item {
        /**
         * Mapping of the JSON object Id
         */
        public static class Id {
            private String kind;
            private String videoId;

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }
        }

        /**
         * Mapping of the JSON object Snippet
         */
        public static class Snippet {

            /**
             * Mapping of the JSON object Thumbnails
             */
            public static class Thumbnails {

                /**
                 * Mapping of the JSON object Thumbnail
                 */
                public static class Thumbnail {
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }

                @JsonProperty("default")
                private Thumbnail _default;
                private Thumbnail medium;
                private Thumbnail high;

                public Thumbnail getDefault() {
                    return _default;
                }

                public void setDefault(Thumbnail _default) {
                    this._default = _default;
                }

                public Thumbnail getMedium() {
                    return medium;
                }

                public void setMedium(Thumbnail medium) {
                    this.medium = medium;
                }

                public Thumbnail getHigh() {
                    return high;
                }

                public void setHigh(Thumbnail high) {
                    this.high = high;
                }
            }

            private Date publishedAt;
            private String channelId;
            private String title;
            private String description;
            private Thumbnails thumbnails;
            private String channelTitle;

            public Date getPublishedAt() {
                return publishedAt;
            }

            public void setPublishedAt(Date publishedAt) {
                this.publishedAt = publishedAt;
            }

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Thumbnails getThumbnails() {
                return thumbnails;
            }

            public void setThumbnails(Thumbnails thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getChannelTitle() {
                return channelTitle;
            }

            public void setChannelTitle(String channelTitle) {
                this.channelTitle = channelTitle;
            }
        }

        private String etag;
        private Id id;
        private Snippet snippet;

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }
    }

    private int results;
    private List<Item> items;

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
