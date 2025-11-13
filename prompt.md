# EventStorming図からDDDコード実装へのプロンプト

## 概要
このドキュメントは、EventStorming図を解析し、既存のDDDアーキテクチャパターンに従ってJava/Spring Bootコードを実装するための指示を定義します。

## EventStorming要素の解釈ルール

### 1. イベント（オレンジ色の付箋）
- **定義**: システムで発生した事実を過去形で表現
- **実装**: Domain層のEntityの状態変更として実装（イベントクラスは作らない）
- **例**: 「ドキュメントが作成された」→ Document entity のコンストラクタまたは状態変更メソッド

### 2. コマンド（青色の付箋）
- **定義**: システムに対する命令・意図
- **ルール**: コマンドが空白の場合、イベントの現在形をコマンドとする
- **実装**: Domain層のEntityのメソッドとして実装（ビジネスロジックはドメインに）
- **例**:
  - 空白の場合: 「ドキュメントが作成された」→「ドキュメントを作成する」→ Document のコンストラクタ
  - 「いいね」→「いいねする」→ Document.markEffective() メソッド

### 3. アクター（黄色の小さい付箋）
- **定義**: システムを操作する人物・ロール
- **実装**: 主にHTTP層のREST APIエンドポイントの実行者として扱う
- **例**: 「ユーザー」→ UserIdをパラメータとして受け取るAPIエンドポイント

### 4. 集約（黄色の大きい付箋）
- **定義**: 関連するエンティティと値オブジェクトのまとまり
- **ルール**: 記載がない場合は、関連するイベントから推測
- **実装**: Domain層のmodels配下の各ドメインモデルディレクトリ
- **推測方法**:
  - イベント名から主要な名詞を抽出
  - 例: 「ドキュメントが作成された」→ Document 集約

### 5. ポリシー（紫/ライラック色の付箋）
- **定義**: ビジネスルール、条件分岐
- **ルール**:
  - 記載ありの場合: Application層のServiceでif文として実装
  - 空白の場合: 次の処理を必ず実行
- **実装**: Application層のServiceクラス内の条件分岐
- **例**: 「期限切れ処理」→ Application層またはbatch層で条件を判定して処理を実行

### 6. リードモデル（緑色の付箋）
- **定義**: 読み取り専用のデータ表現
- **実装**: HTTP層のResponse DTOまたは専用のクエリモデル
- **例**: 「Doc」（緑）→ GetDocumentResponse DTO

### 7. 外部システム（ピンク色の付箋）
- **定義**: 外部サービス・システムとの連携
- **実装**: Infrastructure層の外部サービス連携クラス

## 実装手順

### Step 1: EventStorming図の解析
1. 図内のすべての付箋を色別に分類
2. 付箋間の矢印・フローを把握
3. 空白のコマンドはイベントから生成
4. 集約が不明な場合はイベントから推測

### Step 2: ドメインモデルの定義
1. モジュールと集約ごとにディレクトリを作成
   ```
   src/main/java/org/example/eventstormingsamplecodes/
   └── modules/
       └── {module-name}/           # 例: sns, payment
           └── app/
               ├── domain/models/
               │   └── {aggregate}/  # 例: document
               │       ├── Document.java          # エンティティ
               │       ├── DocumentId.java        # 値オブジェクト（ID）
               │       ├── UserId.java            # 値オブジェクト
               │       └── DocumentRepository.java # リポジトリインターフェース
               ├── application/services/
               │   └── {aggregate}/
               │       └── DocumentApplicationService.java
               ├── infrastucture/persistence/
               │   └── {aggregate}/
               │       ├── datamodel/             # JPA entities
               │       ├── jpa/                   # Spring Data JPA repositories
               │       ├── mapper/                # Domain ↔ DataModel mapper
               │       └── impl/                  # Repository実装
               └── batch/                         # バッチ処理（必要に応じて）
   ```

### Step 3: 各層の実装

#### Domain層 (app/domain/models/)
- エンティティ:
  - コマンドをメソッドとして実装
  - イベントは状態変更として表現
  - ドメイン固有のビジネスルールを実装
  - Lombokの@Getter, @ToString, @EqualsAndHashCodeを活用
- 値オブジェクト:
  - ID型（DocumentId, UserIdなど）をrecordで定義
  - 型安全性を提供
- リポジトリインターフェース:
  - 永続化の抽象化

#### Application層 (app/application/services/)
- ApplicationService:
  - @Serviceアノテーションを付与
  - @Transactionalでトランザクション境界を管理
  - ポリシー（条件分岐）の実装
  - 複数のドメインオブジェクトのオーケストレーション
  - ドメインオブジェクトのメソッドを組み合わせる層

#### Infrastructure層 (app/infrastructure/persistence/)
- datamodel/: JPA Entities
  - @Entity, @Table, @Idなどのアノテーション
  - ドメインモデルとは分離
- jpa/: Spring Data JPA repositories
  - JpaRepositoryを継承
  - カスタムクエリメソッド
- mapper/: Mapper classes
  - DataModel ↔ Domain model変換
- impl/: Repository実装
  - ドメインのRepositoryインターフェースを実装
  - @Repositoryアノテーション

#### HTTP層 (http/)
- controllers/: REST Controllers
  - @RestController, @RequestMappingアノテーション
  - アクター（黄色）に対応するAPIエンドポイント
  - @RequiredArgsConstructorでDI
- models/: Request/Response DTOs
  - Recordクラスを使用
  - リードモデル（緑）を含む

### Step 4: 実装例

以下はdocs/images/sns_traditional.pngの図に基づく実装例です。

#### 値オブジェクト (Value Objects)

```java
// DocumentId.java
package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

public record DocumentId(String value) {
}

// UserId.java
package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

public record UserId(String value) {
}
```

#### ドメインエンティティ (Domain Entity)

```java
// Document.java
package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Document {
    private final DocumentId id;
    private String content;
    private final UserId createdBy;
    private final LocalDateTime createdAt;
    private UserId lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private Set<UserId> effectiveUserIds;

    @Builder
    public Document(DocumentId id, String content, UserId createdBy) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedBy = createdBy;
        this.lastModifiedAt = this.createdAt;
        this.effectiveUserIds = new HashSet<>();
    }

    // イベント: ドキュメントが更新された
    public void update(String content, UserId modifiedBy) {
        this.content = content;
        this.lastModifiedBy = modifiedBy;
        this.lastModifiedAt = LocalDateTime.now();
    }

    // イベント: いいねした
    public void markEffective(UserId userId) {
        effectiveUserIds.add(userId);
    }

    // イベント: いいねを外した
    public void unmarkEffective(UserId userId) {
        effectiveUserIds.remove(userId);
    }

    public boolean isEffectiveFor(UserId userId) {
        return effectiveUserIds.contains(userId);
    }

    public Set<UserId> getEffectiveUserIds() {
        return Collections.unmodifiableSet(effectiveUserIds);
    }
}
```

#### リポジトリインターフェース (Repository Interface)

```java
// DocumentRepository.java
package org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document;

import java.util.List;

public interface DocumentRepository {
    Document save(Document document);
    Document findById(DocumentId id);
    List<Document> findByEffectiveUserId(UserId userId);
}
```

#### アプリケーションサービス (Application Service)

```java
// DocumentApplicationService.java
package org.example.eventstormingsamplecodes.modules.sns.app.application.services.document;

import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.Document;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentRepository;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DocumentApplicationService {
    private final DocumentRepository documentRepository;

    public DocumentApplicationService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional
    public DocumentId createDocument(UserId userId, String content) {
        var documentId = new DocumentId(UUID.randomUUID().toString());
        var document = Document.builder()
                .id(documentId)
                .content(content)
                .createdBy(userId)
                .build();
        documentRepository.save(document);
        return documentId;
    }

    @Transactional
    public void updateDocument(DocumentId documentId, UserId userId, String content) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }
        document.update(content, userId);
        documentRepository.save(document);
    }

    @Transactional
    public void markEffective(DocumentId documentId, UserId userId) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }
        if (document.isEffectiveFor(userId)) {
            return;
        }
        document.markEffective(userId);
        documentRepository.save(document);
    }

    @Transactional
    public void unmarkEffective(DocumentId documentId, UserId userId) {
        var document = documentRepository.findById(documentId);
        if (document == null) {
            throw new DocumentNotFoundException("Document not found");
        }
        if (!document.isEffectiveFor(userId)) {
            return;
        }
        document.unmarkEffective(userId);
        documentRepository.save(document);
    }
}
```

#### REST Controller

```java
// DocumentController.java
package org.example.eventstormingsamplecodes.modules.sns.http.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventstormingsamplecodes.http.controllers.NotFoundException;
import org.example.eventstormingsamplecodes.modules.sns.app.application.services.document.DocumentApplicationService;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.DocumentId;
import org.example.eventstormingsamplecodes.modules.sns.app.domain.models.document.UserId;
import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.jpa.DocumentJpaRepository;
import org.example.eventstormingsamplecodes.modules.sns.http.models.documents.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentApplicationService applicationService;
    private final DocumentJpaRepository documentJpaRepository;

    @GetMapping("/{documentId}")
    public GetDocumentResponse get(@PathVariable String documentId) {
        return documentJpaRepository.findById(documentId)
                .map(GetDocumentResponse::from)
                .orElseThrow(() -> new NotFoundException("Document not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDocumentResponse post(@RequestBody PostDocumentRequest request) {
        var documentId = applicationService.createDocument(
                new UserId(request.userId()),
                request.content()
        );
        return new PostDocumentResponse(documentId.value());
    }

    @PutMapping("/{documentId}")
    public void put(
            @PathVariable String documentId,
            @RequestBody PutDocumentRequest request
    ) {
        applicationService.updateDocument(
                new DocumentId(documentId),
                new UserId(request.userId()),
                request.content()
        );
    }

    @PostMapping("/{documentId}/effective")
    public void markEffective(
            @PathVariable String documentId,
            @RequestBody MarkEffectiveRequest request
    ) {
        applicationService.markEffective(
                new DocumentId(documentId),
                new UserId(request.userId())
        );
    }

    @DeleteMapping("/{documentId}/effective")
    public void unmarkEffective(
            @PathVariable String documentId,
            @RequestBody UnmarkEffectiveRequest request
    ) {
        applicationService.unmarkEffective(
                new DocumentId(documentId),
                new UserId(request.userId())
        );
    }
}
```

#### Request/Response DTOs

```java
// PostDocumentRequest.java
package org.example.eventstormingsamplecodes.modules.sns.http.models.documents;

public record PostDocumentRequest(String userId, String content) {
}

// PostDocumentResponse.java
package org.example.eventstormingsamplecodes.modules.sns.http.models.documents;

public record PostDocumentResponse(String documentId) {
}

// GetDocumentResponse.java - リードモデル（緑の付箋）
package org.example.eventstormingsamplecodes.modules.sns.http.models.documents;

import org.example.eventstormingsamplecodes.modules.sns.app.infrastucture.persistence.document.datamodel.DocumentDataModel;

import java.time.LocalDateTime;
import java.util.Set;

public record GetDocumentResponse(
        String id,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        Set<String> effectiveUserIds
) {
    public static GetDocumentResponse from(DocumentDataModel dataModel) {
        return new GetDocumentResponse(
                dataModel.getId(),
                dataModel.getContent(),
                dataModel.getCreatedBy(),
                dataModel.getCreatedAt(),
                dataModel.getEffectiveUserIds()
        );
    }
}
```

## 使用方法

1. **EventStorming図の取得**: docs/images/配下の画像ファイルを参照
   - `sns_traditional.png`: SNSモジュールの設計
   - `payment_traditional_1.png`, `payment_traditional_2.png`: Paymentモジュールの設計
2. **図の解析**: このプロンプトを参照しながら付箋の色とフローを分析
3. **要素の解釈**: 上記ルールに従って各付箋の意味を理解
4. **実装**: 既存のDDDアーキテクチャパターンに従って実装
5. **推測**: 不明な部分は文脈から推測し、ビジネス的に妥当な実装を行う

## EventStorming図からコード生成のプロンプト例

```
docs/images/sns_traditional.pngの図を解析して、EventStormingの要素に基づいたコードを生成してください。

付箋の色の解釈:
- オレンジ: イベント（ドメインエンティティの状態変更）
- 青: コマンド（ドメインエンティティのメソッド）
- 黄色（小）: アクター（APIエンドポイントの実行者）
- 黄色（大）: 集約（ドメインモデル）
- 緑: リードモデル（Response DTO）

既存のDDDアーキテクチャパターンに従い、以下を生成してください:
1. Domain層: エンティティ、値オブジェクト、リポジトリインターフェース
2. Application層: ApplicationService
3. Infrastructure層: DataModel、Mapper、Repository実装
4. HTTP層: Controller、Request/Response DTO

モジュール名: {module-name}
集約名: {aggregate-name}
```

## 注意事項

- EventStorming図の解釈は文脈依存のため、ビジネス的な妥当性を常に考慮する
- 空白の要素は積極的に推測し、後から修正可能な形で実装する
- 既存のアーキテクチャパターンを崩さないよう注意する
- ドメインモデルとJPA EntityのDataModelは必ず分離する
- 値オブジェクト（ID型など）はJava recordで実装する
- @Transactionalは必ずApplicationService層に配置する
- ControllerではJpaRepositoryを直接使用してもよい（読み取り専用クエリの場合）