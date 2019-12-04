<?php
/*
 * This file is part of PHPUnit.
 *
 * (c) Sebastian Bergmann <sebastian@phpunit.de>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
namespace PHPUnit\Util;

final class RegularExpression
{
    /**
     * @throws \Exception
     *
     * @return false|int
     */
    public static function safeMatch(string $pattern, string $task, ?array $matches = null, int $flags = 0, int $offset = 0)
    {
        $handler_terminator = ErrorHandler::handleErrorOnce();
        $match              = \preg_match($pattern, $task, $matches, $flags, $offset);
        $handler_terminator();

        return $match;
    }
}
