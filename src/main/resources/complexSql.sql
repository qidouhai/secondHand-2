-- 查询每个商品的评论数
SELECT
  goods.*, IFNULL(comment_count.count, 0) 'comment_count'
FROM
  goods
  LEFT JOIN (
              SELECT
                entity_id,
                count(1) AS count
              FROM
                COMMENT
              WHERE
                entity_type = 1
              GROUP BY
                entity_id
            ) AS comment_count ON goods.id = comment_count.entity_id

-- 查询每个商品的积分，并按积分降序排序
SELECT
  goods.*, IFNULL(comment_count.count, 0) * 5 + hot_num * 2 + view_num AS score
FROM
  goods
  LEFT JOIN (
              SELECT
                entity_id,
                count(1) AS count
              FROM
                COMMENT
              WHERE
                entity_type = 1
              GROUP BY
                entity_id
            ) AS comment_count ON goods.id = comment_count.entity_id
ORDER BY
  score DESC

-- 查询指定类目商品的积分，并按积分降序排序，且限制查询的数量
SELECT
  goods.*, IFNULL(comment_count.count, 0) * 5 + hot_num * 2 + view_num AS score
FROM
  goods
  LEFT JOIN (
              SELECT
                entity_id,
                count(1) AS count
              FROM
                COMMENT
              WHERE
                entity_type = 1
              GROUP BY
                entity_id
            ) AS comment_count ON goods.id = comment_count.entity_id
where category_id = 1 and sub_category_id=9
ORDER BY
  score DESC
limit 3

