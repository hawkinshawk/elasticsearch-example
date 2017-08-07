package com.mkyong;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ElasticsearchMigrationTool {

    @Autowired
    private Client client;

    @PostConstruct
    private void init() {
//        if (!client.admin().indices().prepareExists(Constants.BOOK_INDEX_V1).get().isExists()) {
//            createIndex(Constants.BOOK_INDEX_V1);
//        }
//        createAlias(Constants.BOOK_INDEX_V1, Constants.BOOK_INDEX_ALIAS);
                createIndex(Constants.BOOK_INDEX_V2);
                reassignAlias(Constants.BOOK_INDEX_V1, Constants.BOOK_INDEX_V2, Constants.BOOK_INDEX_ALIAS);
                reindex(Constants.BOOK_INDEX_V1, Constants.BOOK_INDEX_V2);
                deleteIndex(Constants.BOOK_INDEX_V1);
    }

    private void createAlias(String indexName, String alias) {
        client.admin().indices().prepareAliases()
                .addAlias(indexName, alias)
                .execute().actionGet();
    }

    private void reassignAlias(String indexNameToBeRemoved,
            String indexNameToBeAdded, String alias) {
        client.admin()
                .indices()
                .prepareAliases()
                .removeAlias(indexNameToBeRemoved, alias)
                .addAlias(indexNameToBeAdded, alias)
                .execute()
                .actionGet();
    }

    private void createIndex(String indexName) {
        client.admin().indices().prepareCreate(indexName).get();
    }

    private void deleteIndex(String indexName) {
        client.admin().indices().prepareDelete(indexName).get();
    }

    private void reindex(String oldIndex, String newIndex) {
        String script = "ctx._source.summary = ctx._source.remove(\"zusammenfassung\")";
        Script groovyMappingScript = new Script(script);
        ReindexAction.INSTANCE
                .newRequestBuilder(client)
                .source(oldIndex)
                .destination(newIndex)
                .script(groovyMappingScript)
                .get();
    }
}
