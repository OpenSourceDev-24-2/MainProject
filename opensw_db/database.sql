SELECT 
    ul.user_id,
    ul.review_id,
    r.facility_id,
    r.user_id AS reviewer_id,
    r.rating,
    r.comment,
    r.created_at
FROM 
    user_likes ul
JOIN 
    reviews r
ON 
    ul.review_id = r.review_id
WHERE 
    ul.user_id = 1;
