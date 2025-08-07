<?php
require_once 'connection.php';

header('Content-Type: application/json');
$conn->set_charset("utf8mb4");

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = json_decode(file_get_contents("php://input"), true);

    if (!isset($input['query']) || empty(trim($input['query']))) {
        echo json_encode([]);
        exit;
    }

    $search = mysqli_real_escape_string($conn, strtolower(trim($input['query'])));
    $searchWords = explode(" ", $search);
    $firstWord = mysqli_real_escape_string($conn, $searchWords[0]);

    $sql = "
        SELECT product_id, product_name 
        FROM Product 
        WHERE 
            LOWER(product_name) LIKE '{$firstWord}%' 
            OR LOWER(product_id) LIKE '{$firstWord}%'
            OR LOWER(product_name) LIKE '%{$search}%'
            OR LOWER(product_id) LIKE '%{$search}%'
        ORDER BY 
            CASE 
                WHEN LOWER(product_name) LIKE '{$firstWord}%' THEN 1
                WHEN LOWER(product_id) LIKE '{$firstWord}%' THEN 1
                ELSE 2
            END
        LIMIT 15
    ";

    $result = mysqli_query($conn, $sql);
    $products = [];

    while ($row = mysqli_fetch_assoc($result)) {
        $products[] = $row;
    }

    echo json_encode($products);
} else {
    echo json_encode([]);
}
?>
